package hfd.msdk.hfdlibrary;

public interface StudentListener {
    // 被监听后要做的是什么
    // 并注册事件
    public void doStudy(Event e);
    // 当名字被改变的事件处理
    public void doChangeName(Event e);
}
