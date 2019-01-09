import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message implements Serializable {
    private String message;
    private List<String> arguments = new ArrayList<>();
    private List<Object[]> tableArguments = new ArrayList<>();

    public Message(String message, List<String> arguments){
        this.arguments = arguments;
        this.message = message;
    }

    public Message(String message, String argument){
        this.message = message;
        this.arguments.add(argument);
    }

    public Message(List<Object[]> arguments){
        this.tableArguments = arguments;
    }

    public Message(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public List<Object[]> getTableArguments() {
        return tableArguments;
    }
}