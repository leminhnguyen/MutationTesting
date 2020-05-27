package components;

import java.util.*;

public class Statement {
    private ArrayList statementTokens = new ArrayList();
    private String description = "";

    public void addToken(Token token) {
        statementTokens.add(token);
    }

    public ArrayList getStatementTokens() {
        return statementTokens;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}