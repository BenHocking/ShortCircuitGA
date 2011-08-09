function plotData(d)
  figure % new plot window
  colors=['r', 'g', 'b', 'c', 'm', 'k'];
  for i = 1:6
    n = i * 50;
    x = load(['trace_', num2str(d), '/fit2_', num2str(n), '.dat']);
    plot((1:length(x)) * 50, x, colors(i));
    hold on;
  end
end
