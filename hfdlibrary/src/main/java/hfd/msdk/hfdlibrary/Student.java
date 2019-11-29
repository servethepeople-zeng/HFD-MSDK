package hfd.msdk.hfdlibrary;

public class Student {
    // student的属性
    private String name;
    // 监听的对象,
    private StudentListener studentListener;
    // 类中被监听的方法
    public void study() {
        // 判断是否注册了监听
        if (studentListener != null) {
            Event e = new Event();
            e.setStudent(this);
            //调用接口中的dostudy方法
            studentListener.doStudy(e);
        }
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
        if(studentListener!=null){
            Event e=new Event();
            e.setStudent(this);
            studentListener.doChangeName(e);
        }
    }
    //注册监听对象
    public void addStudentListener(StudentListener studentListener){
        this.studentListener=studentListener;
    }
}
