package bg.sofia.uni.fmi.mjt.server.commands;

public class GetFoodReportCommand implements Command{
    private String fcdId;

    public GetFoodReportCommand(String fcdId) {
        this.fcdId = fcdId;
    }

    @Override
    public String execute() {
        return "";
    }
}
