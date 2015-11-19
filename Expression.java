import java.util.ArrayList;
import java.util.List;

interface Expression {
    public int evaluate(Robot robot);
}

class Ident implements Expression {
    private String id;

    public Ident(String id) {
        this.id = id;
    }

    public int evaluate(Robot robot) {
        int value = 0;

        try {
            value = lookupVariable(robot);
        } catch (NoSuchVariableException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return value;
    }

    /*
     * Search for a variable with the name 'id' in the robot's VarDecl-list.
     * Return the associated int-value or throw an exception if not in list.
     */
    private int lookupVariable(Robot robot) throws NoSuchVariableException {
        VarDecl decl = null;
        for (VarDecl v : robot.getVarDecls()) {
            if (v.getName().equals(id))
                decl = v;
        }

        if (decl == null)
            throw new NoSuchVariableException(id);
        return decl.getValue();
    }
}

class Number implements Expression {
    private int number;

    public Number(int number) {
        this.number = number;
    }

    public int evaluate(Robot robot) {
        return number;
    }
}

abstract class Operator implements Expression {
    protected Expression left;
    protected Expression right;

    public Operator(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
}

class Plus extends Operator {
    Plus(Expression left, Expression right) {
        super(left, right);
    }

    public int evaluate(Robot robot) {
        return left.evaluate(robot) + right.evaluate(robot);
    }
}

class Minus extends Operator {
    Minus(Expression left, Expression right) {
        super(left, right);
    }

    public int evaluate(Robot robot) {
        return left.evaluate(robot) - right.evaluate(robot);
    }
}

class Times extends Operator {
    Times(Expression left, Expression right) {
        super(left, right);
    }

    public int evaluate(Robot robot) {
        return left.evaluate(robot) * right.evaluate(robot);
    }
}

abstract class Boolean implements Expression {
    protected Expression left;
    protected Expression right;

    public Boolean(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
}


class LargerThan extends Boolean {
    LargerThan(Expression left, Expression right) {
        super(left, right);
    }

    public int evaluate(Robot robot) {
        if (left.evaluate(robot) > right.evaluate(robot))
            return 1;
        else return 0;
    }
}


class SmallerThan extends Boolean {
    SmallerThan(Expression left, Expression right) {
        super(left, right);
    }

    public int evaluate(Robot robot) {
        if (left.evaluate(robot) < right.evaluate(robot))
            return 1;
        else return 0;
    }
}


class Equals extends Boolean {
    Equals(Expression left, Expression right) {
        super(left, right);
    }

    public int evaluate(Robot robot) {
        if (left.evaluate(robot) == right.evaluate(robot))
            return 1;
        else return 0;
    }
}