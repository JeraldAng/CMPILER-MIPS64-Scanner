import javafx.util.Pair;

import java.util.Stack;

// guide sources: https://www.cs.bgu.ac.il/~comp171/wiki.files/01-scanning.pdf
//                https://grrinchas.github.io/posts/dfa-in-java?fbclid=IwAR37iucBV6iKb1Gg_0hj7-nxDDOZ5fLUGbKjW_0RDbjRGoYj6Ho4m_XG7UM

public class DFA {
    private enum States {

        Q0(false), Q1(false), Q2(false), Q3(false), Q4(false), Q5(false), Q6(false), Q7(false), Q8(false), Q9(false), Q10(true),
        Q11(false), Q12(true), Q13(false), Q14(true), Q15(true), Q16(true), Q17(true), Q18(true), Q19(true), Qdead(false),
        Qbottom(false);

        final boolean accept;

        States(boolean accept) {
            this.accept = accept;
        }

        States D, A, I, U, M, L, T, R, F, $, zero, one, two, three, four, five, six, seven, eight, nine;

        // transition states (source -> input -> destination wise)
        static {
            Q0.D = Q1; Q0.R = Q9; Q0.F = Q13; Q0.$ = Q11;
            Q1.A = Q2; Q1.M = Q6;
            Q2.D = Q3;
            Q3.D = Q4;
            Q4.I = Q5; Q4.U = Q17;
            Q5.U = Q17;
            Q6.U = Q7;
            Q7.L = Q8;
            Q8.T = Q16;
            Q16.U = Q17;
            Q9.one = Q10; Q9.two = Q10; Q9.three = Q12; Q9.four = Q18; Q9.five = Q18;
            Q9.six = Q18; Q9.seven = Q18; Q9.eight = Q18; Q9.nine = Q18; Q9.zero = Q18;
            Q10.one = Q18; Q10.two = Q18; Q10.three = Q18; Q10.four = Q18; Q10.five = Q18;
            Q10.six = Q18; Q10.seven = Q18; Q10.eight = Q18; Q10.nine = Q18; Q10.zero = Q18;
            Q11.F = Q13; Q11.one = Q10; Q11.two = Q10; Q11.three = Q12; Q11.four = Q18; Q11.five = Q18;
            Q11.six = Q18; Q11.seven = Q18; Q11.eight = Q18; Q11.nine = Q18; Q11.zero = Q18;
            Q12.zero = Q18; Q12.one = Q18;
            Q13.one = Q14; Q13.two = Q14; Q13.three = Q15; Q13.four = Q19; Q13.five = Q19;
            Q13.six = Q19; Q13.seven = Q19; Q13.eight = Q19; Q13.nine = Q19; Q13.zero = Q19;
            Q14.one = Q19; Q14.two = Q19; Q14.three = Q19; Q14.four = Q19; Q14.five = Q19;
            Q14.six = Q19; Q14.seven = Q19; Q14.eight = Q19; Q14.nine = Q19; Q14.zero = Q19;
            Q15.zero = Q19; Q15.one = Q19;
        }

        States transition(char symbol) {
            switch (symbol) {
                case 'D':
                    return this.D;
                case 'A':
                    return this.A;
                case 'I':
                    return this.I;
                case 'U':
                    return this.U;
                case 'M':
                    return this.M;
                case 'L':
                    return this.L;
                case 'T':
                    return this.T;
                case 'R':
                    return this.R;
                case 'F':
                    return this.F;
                case '$':
                    return this.$;
                case '0':
                    return this.zero;
                case '1':
                    return this.one;
                case '2':
                    return this.two;
                case '3':
                    return this.three;
                case '4':
                    return this.four;
                case '5':
                    return this.five;
                case '6':
                    return this.six;
                case '7':
                    return this.seven;
                case '8':
                    return this.eight;
                case '9':
                    return this.nine;
                default:
                    return Qdead;       // Qdead if input is not in the alphabet
            }
        }
    }

    public String identifyState(String word) {
        // maximal munch code, return the final state
        Stack<Pair<States, Integer>> stack = new Stack<>();
        int i = 0;

        while (true) {
            States state = States.Q0;
            Pair<States, Integer> pair = new Pair<>(States.Qbottom, i);
            stack.push(pair);

            while (i < word.length() && state.transition(word.charAt(i)) != null) {
                if (state.transition(word.charAt(i)).accept) {
                    stack.empty();
                }
                pair = new Pair<>(state, i);
                state = state.transition(word.charAt(i));
                stack.push(pair);

                i++;
            }

            while (!state.accept) {
                if (stack.isEmpty()){
                    return state.toString();
                }

                Pair<States, Integer> top = stack.pop();

                if (top == new Pair<>(States.Qbottom, 0)) {
                    return state.toString();
                }
            }

            if (i >= word.length()) {
                return state.toString();
            }
        }
    }
}
