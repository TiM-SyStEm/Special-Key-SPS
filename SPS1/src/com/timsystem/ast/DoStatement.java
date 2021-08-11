package com.timsystem.ast;

public class DoStatement implements Statement {
    private final Statement statement;

    public DoStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public void execute() {
        while (true){
            try{statement.execute();}
            catch (StopStatement st){break;}
        }
    }
    @Override
    public String toString(){
        return "do " + statement;
    }
}
