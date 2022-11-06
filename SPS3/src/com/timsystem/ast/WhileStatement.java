package com.timsystem.ast;

public class WhileStatement implements Statement {
    private final Expression condition;
    private final Statement statement;

    public WhileStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void execute() {
        while (condition.eval().asNumber() != 0) {
            try {
                statement.execute();
            } catch (StopStatement st) {
                break;
            } catch (ContinueStatement ct) {
                continue;
            }
        }
    }

    @Override
    public String toString() {
        return "while " + condition + " " + statement;
    }
}
