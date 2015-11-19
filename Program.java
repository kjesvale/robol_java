import java.util.ArrayList;
import java.util.List;

interface Robol {
    public void interpret();
}

class Program implements Robol {
    private Grid grid;
    private Robot robot;

    public Program(Grid grid, Robot robot) {
        this.grid = grid;
        this.robot = robot;
    }

    public void interpret() {
        robot.setGrid(grid);
        robot.interpret();
    }
}

/*
 * The robot object is passed on to elements in the abstract syntax tree
 * as an argument of the interpret()-function. The elements then modify
 * variables of the robot using set-functions.
 */
class Robot implements Robol {
    private List<VarDecl> varDecls = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();
    private Start start;

    private Grid grid;
    private int x;
    private int y;

    public Robot(List<VarDecl> varDecls, Start start, 
                 List<Statement> statements) {
        this.varDecls = varDecls;
        this.start = start;
        this.statements = statements;
    }

    public void interpret() {
        for (VarDecl v : varDecls)
            v.interpret(this);

        start.interpret(this);

        for (Statement s : statements)
            s.interpret(this);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Grid getGrid() { return grid; }
    public List<VarDecl> getVarDecls() { return varDecls; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setGrid(Grid grid) { this.grid = grid; }
}

enum Direction {
    North, East, South, West
}

abstract class Position {
    protected int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}

class Start extends Position {
    public Start(int x, int y) {
        super(x, y);
    }

    public void interpret(Robot robot) {
        robot.setX(x);
        robot.setY(y);
    }
}

class Grid extends Position {
    public Grid(int x, int y) {
        super(x, y);
    }

    public boolean outOfBounds(int rx, int ry) {
        if (x < 0 || rx > x || y < 0 || ry > y)
            return true;
        else return false;
    }
}

/*
 * Evaluate the expression when first interpreted. On access, using the
 * getValue()-method, return the resulting integer value.
 */
class VarDecl {
    private String name;
    private Expression exp;
    private int value;

    public VarDecl(String name, Expression exp) {
        this.name = name;
        this.exp = exp;
    }

    public void interpret(Robot robot) {
        this.value = exp.evaluate(robot);
    }

    public String getName() { return name; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
}

/* Is thrown if robot falls off grid. */
class OutOfBoundsException extends Exception {
    public OutOfBoundsException(int gx, int gy, int x, int y) {
        super(String.format("Fell out of grid (%d, %d) at position (%d, %d)!",
                            gx, gy, x, y));
    }
}

/* Is thrown if a certain identifier is not found in a robot's VarDecl-list */
class NoSuchVariableException extends Exception {
    public NoSuchVariableException(String identifier) {
        super(String.format("Variable \"%s\" was not found.", identifier));
    }
}