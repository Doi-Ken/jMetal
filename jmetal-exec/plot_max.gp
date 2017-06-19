set ticslevel 0
set format x ""
set format y ""
set format z ""
graph="graph0"
unset xtics
unset ytics
unset ztics
set parametric 
set angle degree
set urange [0:1]
set vrange [0:1]
set xrange [0:560]
set yrange [0:560]
set zrange [0:560]
set view 45,135
set isosample 10, 10
unset key
splot  551.0 * u, 551.0 * v, 551.0 * (1.0 - u - v) linecolor rgb "gray70" linewidth 7, graph.".txt" using (-1 * $1):(-1 * $2):(-1 * $3) with points pointtype 7 linecolor rgb "red" ps 1.5, graph.".txt" using (-1 * $1):(-1 * $2):(-1 * $3) with points pointtype 6 linecolor rgb "black" ps 1.5 linewidth 2.0
set terminal emf
set border 2 + 8 + 32 
set output graph.".emf"
replot
