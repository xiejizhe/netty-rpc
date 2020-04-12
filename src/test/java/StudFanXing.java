
public class StudFanXing<T> {

    public StudFanXing(T data){
        this.data=data;
    }

    private  T data;

    public T getData(){
        return data;
    }

    public void  getData1(StudFanXing<?> stu){

        System.err.println(stu.getData());
    }

}
