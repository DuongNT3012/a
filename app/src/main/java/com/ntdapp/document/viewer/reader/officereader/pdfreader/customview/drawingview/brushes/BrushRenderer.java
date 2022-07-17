package com.ntdapp.document.viewer.reader.officereader.pdfreader.customview.drawingview.brushes;


import android.graphics.Canvas;

import com.ntdapp.document.viewer.reader.officereader.pdfreader.customview.drawingview.DrawingEvent;

public interface BrushRenderer {
    void draw(Canvas canvas);
    void onTouch(DrawingEvent drawingEvent);
    void setBrush(Brush brush);
}
