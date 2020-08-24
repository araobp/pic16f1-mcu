package jp.araobp.amg8833.`interface`

import android.content.Context
import android.util.Log
import jp.araobp.twelite.ITweiliteReceiver
import jp.araobp.twelite.TweliteMessage
import jp.araobp.twelite.TweliteDriver
import java.util.*

class Amg8833Interface(context: Context, dst: Byte, val receiver: IAmg8833Receiver) {

    companion object {
        val TAG: String = this::class.java.simpleName
        val CMD_P = byteArrayOf('p'.toByte())
    }

    private var mTimer: Timer = Timer()
    private var mTweliteReceiver: TweliteDriver

    init {

        mTweliteReceiver = TweliteDriver(context,
            object : ITweiliteReceiver {
                override fun onMessage(message: TweliteMessage) {
                    Log.d(TAG, message.toString())
                    receiver.onAmg8833Data(Amg8833Data(message.data))
                }
            }
        )

        mTimer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    mTweliteReceiver.send(dst, CMD_P)
                }
            },
            1000, 100
        )

    }

    fun destroy() {
        mTimer.cancel()
        mTweliteReceiver.destroy()
    }

}
