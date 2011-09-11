function allVars = getAllVars(first, last)
  vars=getVars();
  allVars=zeros(length(first:last), length(vars));
  lineIdx=0;
  for i=first:last
    dirname=['randVol_',num2str(i)];
    script=[dirname,'/trace_full.nj'];
    line=zeros(1,length(vars));
    for j=1:length(vars)
      v=readVar(script,char(vars(j)));
      line(1,j)=v;
    end
    allVars(++lineIdx,:)=line;
  end
end