package JsonBean;

public class result {
    private int Result;     // 返回请求结果类型
    private String Content;     // 返回详细内容，josn格式
    private String Msg;       // 返回消息

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }


    @Override
    public String toString() {
        return "result{" +
                "Result=" + Result +
                ", Content=" + Content +
                ", Msg='" + Msg + '\'' +
                '}';
    }
}
