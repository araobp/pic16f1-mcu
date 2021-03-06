package jp.araobp.mpu9250.analyzer

import android.graphics.Color
import android.view.SurfaceView
import jp.araobp.mpu9250.serial.Mpu9250Data

class Oscilloscope(val surfaceView: SurfaceView, val maxNumEntries: Int) {

    private val bufRecords = arrayOf(
        ArrayList<Short>(maxNumEntries),
        ArrayList(maxNumEntries),
        ArrayList(maxNumEntries),
        ArrayList(maxNumEntries),
        ArrayList(maxNumEntries),
        ArrayList(maxNumEntries)
    )

    private val colorsLine = arrayOf(RED_STROKE, GREEN_STROKE, YELLOW_STROKE, BLUE_STROKE, MAGENTA_STROKE, CYAN_STROKE)
    private val colorsText = arrayOf(RED_TEXT, GREEN_TEXT, YELLOW_TEXT, BLUE_TEXT, MAGENTA_TEXT, CYAN_TEXT)

    fun update(data: Mpu9250Data) {

        val array = arrayOf(
            data.ax,
            data.ay,
            data.az,
            data.gx,
            data.gy,
            data.gz
        )

        bufRecords.forEachIndexed { axis, shorts ->
            // Bounded array list
            if (shorts.size >= maxNumEntries) {
                shorts.removeAt(0)
            }
            shorts.add(array[axis])
        }

        val canvas = surfaceView.holder.lockCanvas()

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        canvas.drawColor(Color.BLACK)
        val xRatio = width / maxNumEntries.toFloat()
        val yZeroLine = height / 2F
        val yMax = yZeroLine * 9F / 10F
        val yMaxLine = yZeroLine - yMax
        val yMinLine = yZeroLine + yMax
        val scale = yMax / Short.MAX_VALUE

        // Y zero line
        canvas.drawLine(
            0F,
            yZeroLine,
            width,
            yZeroLine,
            DARK_GRAY_STROKE
        )

        // Y max/min lines
        canvas.drawLine(
            0F,
            yMaxLine,
            width,
            yMaxLine,
            DARK_GRAY_STROKE
        )
        canvas.drawLine(
            0F,
            yMinLine,
            width,
            yMinLine,
            DARK_GRAY_STROKE
        )

        bufRecords.forEachIndexed { axis, shorts ->
            for (i in shorts.size - 1 downTo 1) {
                val v = -shorts[i] * scale + yZeroLine
                val vNext = -shorts[i - 1] * scale + yZeroLine
                canvas.drawLine(
                    (shorts.size - i - 1).toFloat() * xRatio,
                    v,
                    (shorts.size - i).toFloat() * xRatio,
                    vNext,
                    colorsLine[axis]
                )
            }
        }

        // Graph legends
        // Graph legends
        val textStartX = width - 100
        canvas.drawText("ax", textStartX, height - 300, colorsText[0])
        canvas.drawText("ay", textStartX, height - 250, colorsText[1])
        canvas.drawText("az", textStartX, height - 200, colorsText[2])
        canvas.drawText("gx", textStartX, height - 150, colorsText[3])
        canvas.drawText("gy", textStartX, height - 100, colorsText[4])
        canvas.drawText("gz", textStartX, height - 50, colorsText[5])

        surfaceView.holder.unlockCanvasAndPost(canvas)
    }
}