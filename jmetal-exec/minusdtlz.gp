set ticslevel 0
set format x ""
set format y ""
set format z ""
graph="norm_graph0"
set xtics 0, 0.5, 1.0
set ytics 0, 0.5, 1.0
set ztics 0, 0.5, 1.0
set parametric 
set angle degree
set urange [0:1.0]
set vrange [0:1.0]
set xrange [0:1.2]
set yrange [0:1.2]
set zrange [0:1.2]
set view 60,135
set isosample 10, 10
set size 0.7, 1
unset key
splot  1 - u * v, 1 - u * (1-v), u linecolor rgb "gray70" linewidth 7, graph.".txt" with points pointtype 7 linecolor rgb "red" ps 1.5, graph.".txt" with points pointtype 6 linecolor rgb "black" ps 1.5 linewidth 2.0
set terminal emf
set border 1 + 2 + 4 + 8 + 16 + 32
set output graph.".emf"
replot
