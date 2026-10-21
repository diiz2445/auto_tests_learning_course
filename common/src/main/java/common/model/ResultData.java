package common.model;
import java.util.Map;
public class ResultData {
    private String message; private Map<String, Long> data;
    public ResultData() {}
    public Long getDataId() { return data == null ? null : data.get("id"); }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, Long> getData() { return data; }
    public void setData(Map<String, Long> data) { this.data = data; }
    @Override public String toString() { return "ResultData{message='" + message + "', data=" + data + '}'; }
}
