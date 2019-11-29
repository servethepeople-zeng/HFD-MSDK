package hfd.msdk.hfdlibrary;

public class Demo2 {
    public static void main(String[] args) {
        //创建被监听的类
        Student student=new Student();
        //给目标对象添加监听
        student.addStudentListener(new StudentListener() {
            @Override
            public void doStudy(Event e) {
                //重写监听的方法
                System.out.println("监听是否学习................"+e.getStudent());
            }
            @Override
            public void doChangeName(Event e) {
                System.out.println("当改变属性name时................"+e.getStudent().getName());
            }
        });
        student.study();
        student.setName("donghongyujava");
    }
}
