# Emacs Keybinds Reference

Workflow for a CIDER session in a Clojure Project:

- Open the source code file within the src/<classname> folder
- `M-x cider-jack-in` to start a CIDER session and open a REPL
- `C-c C-z` to switch between REPL and Code
- `C-c C-k` to compile the current Clojure file
- `q` when there is an exception in the CIDER REPL to close the stack trace
- `C-x C-e` to evaluate the expression immediately preceding the cursor
- `C-x M-e` to evaluate the form preceding the point and output the result to the REPL
- `C-↑, C-↓` to cycle up and down through the REPL history
- `C-c C-d C-d` to display documentation for symbol under point
- `C-c Spc` to Vertically align a Sexp
- `C-c M-n M-n` to switch the namespace of the REPL buffer to the namespace of the current buffer.

Paredit Commands:

- `M-(` to wrap the expression after the point with paranthese. eg: `(* 1 |2 3 4)` becomes `(* 1 (|2) 3 4)` where `|` is the current point.
- `C-→` to Slurp; move closing parenthesis to the right to include next expression.
- `C-←` to Barf; move closing parenthesis to the left to exclude last expression.

## Reference

https://www.braveclojure.com/basic-emacs/
