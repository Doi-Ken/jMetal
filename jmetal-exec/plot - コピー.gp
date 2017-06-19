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
set view 45,135
set isosample 10, 10
unset key
splot  graph.".txt" with points pointtype 7 linecolor rgb "red" ps 1.5, graph.".txt" with points pointtype 6 linecolor rgb "black" ps 1.5 linewidth 2.0
set terminal emf
set border 2 + 8 + 32 
set output graph.".emf"
replot
