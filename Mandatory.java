import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Create ASTs of the example programs and interpret them. */
public class Mandatory {
    public Mandatory() {    
       
        Program prog1 = new Program(
            new Grid(64, 64),
            new Robot(
                new ArrayList<VarDecl>(),
                new Start(23, 30),
                new ArrayList<Statement>(Arrays.asList(
                    new Move(Direction.West, new Number(15)),
                    new Move(Direction.South, new Number(15)),
                    new Move(Direction.East, new Plus(new Number(2), new Number(3))),
                    new Move(Direction.North, new Plus(new Number(10), new Number(27))),
                    new Stop()))));
        
        Program prog2 = new Program(
            new Grid(64, 64),
            new Robot(
                new ArrayList<VarDecl>(Arrays.asList(
                    new VarDecl("i", new Number(5)),
                    new VarDecl("j", new Number(3)))),

                new Start(23, 6),
                new ArrayList<Statement>(Arrays.asList(
                    new Move(Direction.North, new Times(new Number(3), new Ident("i"))),
                    new Move(Direction.East, new Number(15)),
                    new Move(Direction.South, new Number(4)),
                    new Move(Direction.West, new Plus(new Plus(
                                                new Times(new Number(2), new Ident("i")),
                                                new Times(new Number(3), new Ident("j"))),
                                             new Number(5))),
                    new Stop()))));

        Program prog3 = new Program(
            new Grid(64, 64),
            new Robot(
                new ArrayList<VarDecl>(Arrays.asList(
                    new VarDecl("i", new Number(5)),
                    new VarDecl("j", new Number(3)))),

                new Start(23, 6),
                new ArrayList<Statement>(Arrays.asList(
                    new Move(Direction.North, new Times(new Number(3), new Ident("i"))),
                    new Move(Direction.West, new Number(15)),
                    new Move(Direction.East, new Number(4)),

                    new While(new LargerThan(new Ident("j"), new Number(0)), 
                              new ArrayList<Statement>(Arrays.asList(
                        new Move(Direction.South, new Ident("j")),
                        new Assignment("j", new Minus(new Ident("j"), new Number(1)))))),
                    new Stop()))));

        Program prog4 = new Program(
            new Grid(64, 64),
            new Robot(
                new ArrayList<VarDecl>(Arrays.asList(
                    new VarDecl("j", new Number(3)))),
                new Start(1, 1),
                new ArrayList<Statement>(Arrays.asList(
                    new While(new LargerThan(new Ident("j"), new Number(0)),
                              new ArrayList<Statement>(Arrays.asList(
                        new Move(Direction.North, new Number(1))))),
                    new Stop()))));

        prog1.interpret(); // solution: (13, 52)
        prog2.interpret(); // solution: (14, 17)
        prog3.interpret(); // solution: (12, 15)
        prog4.interpret(); // no solution (out of bounds)
    }

    public static void main(String[] args) {
        new Mandatory();
    }
}