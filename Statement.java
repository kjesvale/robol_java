import java.util.ArrayList;
import java.util.List;

interface Statement {
    public void interpret(Robot robot);
}

class Move implements Statement {
    private Direction dir;
    private Expression exp;

    public Move(Direction dir, Expression exp) {
        this.dir = dir;
        this.exp = exp;
    }

    public void interpret(Robot robot) {
        try {
            int rx = robot.getX();
            int ry = robot.getY();

            switch(dir) {
                case North: robot.setY(ry + exp.evaluate(robot)); break;
                case East:  robot.setX(rx + exp.evaluate(robot)); break;
                case South: robot.setY(ry - exp.evaluate(robot)); break;
                case West:  robot.setX(rx - exp.evaluate(robot)); break;
            }

            Grid g = robot.getGrid();
            if (g.outOfBounds(rx, ry))
                throw new OutOfBoundsException(g.getX(), g.getY(), rx, ry);

        } catch (OutOfBoundsException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}

class Assignment implements Statement {
    private String name;
    private Expression exp;

    public Assignment(String name, Expression exp) {
        this.name = name;
        this.exp = exp;
    }

    /*
     * Find the variable declaration with identifier 'name' and evaluate the
     * stored expression as its value.
     */
    public void interpret(Robot robot) {
        try {
            VarDecl decl = lookupName(robot);
            decl.setValue(exp.evaluate(robot));
        } catch (NoSuchVariableException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /*
     * Search for a variable declaration with identifier 'name' and return it.
     * If not found, throw an exception.
     */
    private VarDecl lookupName(Robot robot) throws NoSuchVariableException {
        List<VarDecl> varDecls = robot.getVarDecls();
        VarDecl decl = null;
        for (VarDecl v : varDecls) {
            if (v.getName().equals(name))
                decl = v;
        }

        if (decl == null)
            throw new NoSuchVariableException(name);
        return decl;
    }
}

class While implements Statement {
    private Boolean condition;
    private List<Statement> statements;

    public While(Boolean condition, List<Statement> statements) {
        this.condition = condition;
        this.statements = statements;
    }

    public void interpret(Robot robot) {
        while (condition.evaluate(robot) == 1) {
            for (Statement s : statements)
                s.interpret(robot);
        }
    }
}

class Stop implements Statement {
    public void interpret(Robot robot) {
        System.out.format("Robot stopped at position (%d, %d)\n", 
                            robot.getX(), robot.getY());
    }
}