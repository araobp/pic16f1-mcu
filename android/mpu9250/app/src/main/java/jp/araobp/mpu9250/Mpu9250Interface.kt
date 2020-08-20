package jp.araobp.mpu9250

import android.content.Context
import android.util.Log
import jp.araobp.uart.UsbSerial
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Mpu9250Interface(context: Context, baudrate: Int, val receiver: IDataReceiver): UsbSerial(context, baudrate, 8, 8, 0, 0)
{

    /**
     * Data format
     *
     * In case of TYPE == 0xA0:
     * [TYPE(0xA0)][SEQH][SEQL][ACLH][ACLL][GYRH][GYRL]
     *
     * In case of SENS == 0xA1:
     * [TYPE(0xA1)][MAGH][MAGL]
     */

    companion object {
        val TAG: String = this::class.java.simpleName

        const val TYPE_MPU9250 = 0xA0.toByte()
        const val TYPE_AK8963 = 0xA1.toByte()

        const val HEADER_H = 0xA5.toByte()
        const val HEADER_L = 0x5A.toByte()

        val ACCEL_RESOLUTION = 2F / 32768F
        val GYRO_RESOLUTION = 250F / 32768F
        val MAGNETO_RESOLUTION = 0.15F  // micro Tesla
    }

    enum class AccelRange {
        G_2,
        G_4,
        G_8,
        G_16
    }

    enum class GyroRange {
        DPS_250,
        DPS_500,
        DPS_1000,
        DPS_2000
    }

    private enum class State {
        HEADER_DETECTING,
        TYPE_MPU9250_RECEIVING,
        TYPE_AK8963_RECEIVING
    }

    private var mIdx = 0
    private var state:State = State.HEADER_DETECTING
    private var bodyMpu9250Data = ArrayList<Byte>()
    private var bodyAk8963Data = ArrayList<Byte>()

    fun toShort(h: Byte, l: Byte): Short {
        val byteBuffer: ByteBuffer = ByteBuffer.allocate(2)
        byteBuffer.order(ByteOrder.BIG_ENDIAN)
        byteBuffer.put(h)
        byteBuffer.put(l)
        return byteBuffer.getShort(0)
    }

    fun setAccelRange(range: AccelRange) = tx(byteArrayOf(
        HEADER_H,
        HEADER_L,
        'r'.toByte(),
        'a'.toByte(),
        range.ordinal.toByte()
    ))

    fun setGyroRange(range: GyroRange) = tx(byteArrayOf(
        HEADER_H,
        HEADER_L,
        'r'.toByte(),
        'g'.toByte(),
        range.ordinal.toByte()
    ))

    override fun parse(messageFraction: ByteArray, len: Int) {

        for (i in 0 until len) {
            val b = messageFraction[i]

            when (state) {
                State.HEADER_DETECTING -> {
                    when (b) {
                        TYPE_MPU9250 -> {
                            state = State.TYPE_MPU9250_RECEIVING
                            bodyMpu9250Data.clear()
                            mIdx = 0
                        }
                        TYPE_AK8963 -> {
                            state = State.TYPE_AK8963_RECEIVING
                            bodyAk8963Data.clear()
                            mIdx = 0
                        }
                    }
                }

                State.TYPE_MPU9250_RECEIVING -> {
                    bodyMpu9250Data.add(b)
                    if (++mIdx == 14) {
                        val seq =  bodyMpu9250Data[0].toUInt().shl(8) + bodyMpu9250Data[1].toUInt()
                        val ax = toShort(bodyMpu9250Data[2], bodyMpu9250Data[3]) * ACCEL_RESOLUTION
                        val ay = toShort(bodyMpu9250Data[4], bodyMpu9250Data[5]) * ACCEL_RESOLUTION
                        val az = toShort(bodyMpu9250Data[6], bodyMpu9250Data[7]) * ACCEL_RESOLUTION
                        val gx = toShort(bodyMpu9250Data[8], bodyMpu9250Data[9]) * GYRO_RESOLUTION
                        val gy = toShort(bodyMpu9250Data[10], bodyMpu9250Data[11]) * GYRO_RESOLUTION
                        val gz = toShort(bodyMpu9250Data[12], bodyMpu9250Data[13]) * GYRO_RESOLUTION
                        val data = Mpu9250Data(seq, ax, ay, az, gx, gy, gz)
                        Log.d(TAG, data.toString())
                        receiver.onMpu9250Data(data)
                        state = State.HEADER_DETECTING
                    }
                }

                State.TYPE_AK8963_RECEIVING -> {
                    bodyAk8963Data.add(b)
                    if (++mIdx == 8) {
                        val seq =  bodyAk8963Data[0].toUInt().shl(8) + bodyAk8963Data[1].toUInt()
                        val mx = toShort(bodyAk8963Data[2], bodyAk8963Data[3]) * MAGNETO_RESOLUTION
                        val my = toShort(bodyAk8963Data[4], bodyAk8963Data[5]) * MAGNETO_RESOLUTION
                        val mz = toShort(bodyAk8963Data[6], bodyAk8963Data[7]) * MAGNETO_RESOLUTION
                        val data = Ak8963Data(seq, mx, my, mz)
                        Log.d(TAG, data.toString())
                        receiver.onAk8963Data(data)
                        state = State.HEADER_DETECTING
                    }
                }
            }

        }
    }
}