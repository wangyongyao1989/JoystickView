# JoystickView
## GamepadView、JostickView仿创客工场中的遥感的自定义View
* 首先自定义View的三大步骤 onMeasure()、onDraw()、onLayout()，在此控件中放置对应的图片即可实现。onLayout的放置似乎没有什么必要，
最主要的是onDraw绘制出控件。
* 绘制出控件接下来就是onTouchEvent()对手势的事件进行处理，包含中心遥感随着手势的点击、拖动实时位置的变化，手势释放时中心遥感回到原始位置。
* 以上处理完之后就是逻辑的处理了，根据手势的事件在View所表现出来的坐标计算出对应的方向并与方向箭头挂上关系。
