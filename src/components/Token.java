package components;

import java.util.*;

public class Token {
    
    private String token;
    private String description;
    private int lineNumber;

    public Token(){
        token = "";
        description = "";
        lineNumber = 0;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
       return token + "\t" + description + "\n";
    }

}