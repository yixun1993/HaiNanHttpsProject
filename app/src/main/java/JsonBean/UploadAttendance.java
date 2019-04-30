package JsonBean;

import java.util.List;

public class UploadAttendance {

    private int Count;
    private List<Uploadlogs> logs;
    public void setCount(int Count) {
        this.Count = Count;
    }
    public int getCount() {
        return Count;
    }

    public void setLogs(List<Uploadlogs> logs) {
        this.logs = logs;
    }
    public List<Uploadlogs> getLogs() {
        return logs;
    }
}
