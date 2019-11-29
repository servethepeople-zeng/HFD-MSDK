package hfd.msdk.hfdlibrary;

public class Event {
    // 创建需要处理事件的目标类
    private Student student;
    // 事件处理方法
    // 返回被处理事件的类
    public Student getStudent() {
        return student;
    }
    // 为被处理事件的类进行赋值的操作
    public void setStudent(Student student) {
        this.student = student;
    }
}
