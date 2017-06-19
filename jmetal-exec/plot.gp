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
set urange [0:360]
set vrange [0:360]
set xrange [0:3]
set yrange [0:5]
set zrange [0:7]
set view 45,135
set isosample 10, 10
unset key
splot  "WFG4.3D.pf" linecolor rgb "gray70" linewidth 7, graph.".txt" with points pointtype 7 linecolor rgb "red" ps 1.5, graph.".txt" with points pointtype 6 linecolor rgb "black" ps 1.5 linewidth 2.0
set terminal emf
set border 2 + 8 + 32 
set output graph.".emf"
replot
