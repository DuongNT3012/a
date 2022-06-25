package com.document.officereader.customview.drawingview.brushes;


import android.graphics.Canvas;

import com.document.officereader.customview.drawingview.DrawingEvent;

public interface BrushRenderer {
    void draw(Canvas canvas);
    void onTouch(DrawingEvent drawingEvent);
    void setBrush(Brush brush);
}
