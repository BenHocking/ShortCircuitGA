function [falsePos, falseNeg] = falses(threshold, target, pre, post)

  falsePos = 0;
  falseNeg = 0;
  for i=1:length(pre)
        tf = post(i);
        meanFit = pre(i);
        if (tf > target)
          if (meanFit < threshold) # false negative
            falseNeg = falseNeg + 1;
          endif
	else
	  if (meanFit >= threshold) # false positive
            falsePos = falsePos + 1;
          endif
        endif
  end

end