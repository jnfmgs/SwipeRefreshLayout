# SwipeRefreshLayout
SwipeRefreshLayout
关于Google推出的下拉刷新控件SwipeRefreshLayout的相关使用方法，大家可以去参考http://blog.csdn.net/geeklei/article/details/38876981，本文也借鉴了其中的一些内容和“颜路的博客”中《官方下拉刷新SwipeRefreshLayout增加上拉加载更多》一文。


话不多说，直接先上改造效果图（截屏时卡，凑合看吧）：

下拉刷新和上拉加载

![github](http://img.blog.csdn.net/20140831122158921?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjAzNjgxMw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
![github](http://img.blog.csdn.net/20140831122548872?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjAzNjgxMw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

简单讲下原始代码的原理：

下拉时，计算手指移动距离，如果超过一个系统默认的临界值mTouchSlop，该事件就不下发到子控件进行处理，而是SwipeRefreshLayout自己处理。

变量mDistanceToTriggerSync指定了下拉刷新的临界值，如果下拉距离没有大于该值，则计算下拉距离和mDistanceToTriggerSync的比值，并用该值作为进度百分比对进度条mProgressBar进行设置，同时移动子控件（ListView之类）的位置，屏幕上可以看到进度条颜色缓慢拉长的动画，同时子控件向下移动。

如果下拉距离大于mDistanceToTriggerSync，则设置动画把子控件位置复位，然后启动下拉刷新的色条循环动画，并执行下拉刷新的监听事件。


关于进度条SwipeProgressBar的动画显示，Google的代码里埋藏了一个坑人的陷阱。现象就是如果你在底部加了进度条，动画效果异常，不会出现渐变的色条，只是生硬的转换。上面参考的文章里也碰到了这个问题。其实原因很简单，看下图：

![github](http://img.blog.csdn.net/20140831124214107?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjAzNjgxMw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

把进度条SwipeProgressBar的高度设置大了后，可以看出其动画效果是在进度条的中心向外部循环画圆，每个循环中圆的颜色不同。重点是圆心的位置。

看SwipeProgressBar的如下代码，会发现在计算圆心高度cy的时候，取值是进度条高度的一半，这样的话圆心会一直在上面，底部进度条自然动画异常

    void draw(Canvas canvas) {  
    final int width = mBounds.width();  
    final int height = mBounds.height();  
    final int cx = width / 2;  
    final int cy = height / 2;  
    boolean drawTriggerWhileFinishing = false;  
    int restoreCount = canvas.save();  
    canvas.clipRect(mBounds);  
    
修改SwipeProgressBar的代码，使其圆心在所在进度条的中心：

    void draw(Canvas canvas) {  
        final int width = mBounds.width();  
        final int height = mBounds.height();  
        final int cx = width / 2;  
        //final int cy = height / 2;  
        final int cy = mBounds.bottom - height / 2;  
        boolean drawTriggerWhileFinishing = false;  
        int restoreCount = canvas.save();  
        canvas.clipRect(mBounds);
        
效果如图：

![github](http://img.blog.csdn.net/20140831125234801?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMjAzNjgxMw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

明白了原始代码的原理，就好入手进行修改了，修改的代码会在后面贴出来，注释很详细，这里就不具体分析了。

下面看修改后的功能：

1.可设置是否打开下拉刷新功能，可设置是否打开上拉加载功能，默认全部打开。

2.可设置是否在数据不满一屏的情况下打开上拉加载功能，默认关闭。

3.可单独设置上下进度条的颜色，也可同时设置一样的颜色。
