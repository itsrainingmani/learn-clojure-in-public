# Emacs Keybinds Reference

Workflow for a CIDER session in a Clojure Project:

- Open the source code file within the src/<classname> folder
- `M-x cider-jack-in` to start a CIDER session and open a REPL
- `C-c C-z` to switch between REPL and Code
- `C-c C-k` to compile the current Clojure file
- `q` when there is an exception in the CIDER REPL to close the stack trace
- `C-x C-e` to evaluate the expression immediately preceding the cursor
- `C-↑, C-↓` to cycle up and down through the REPL history
- `C-c C-d C-d` to display documentation for symbol under point
0 `C-c Spc` to Vertically align a Sexp

Paredit Commands:

- `M-(` to wrap the expression after the point with paranthese. eg: `(* 1 |2 3 4)` becomes `(* 1 (|2) 3 4)` where `|` is the current point.
- `C-→` to Slurp; move closing parenthesis to the right to include next expression.
- `C-←` to Barf; move closing parenthesis to the left to exclude last expression.

## Reference

https://www.braveclojure.com/basic-emacs/
