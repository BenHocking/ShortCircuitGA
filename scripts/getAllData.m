function data = getAllData(first, last)
    data=zeros(length(first:last), 122);
    trials=50:50:300;
    lineIdx=0;
    for i=first:last
        dirname=['randVol_',num2str(i)];
        line=zeros(1,122);
        d=load([dirname,'/fit_tst_mean_act.dat']);
        line(1:15)=d';
        d=load([dirname,'/fit_tst_ssd_act.dat']);
        line(16)=d;
        d=load([dirname,'/fit_trn_mean_act.dat']);
        line(17:31)=d';
        d=load([dirname,'/fit_trn_ssd_act.dat']);
        line(32)=d;
        colIdx=33;
        for trial=trials;
            d=load([dirname,'/fit2_',num2str(trial),'.dat']);
            line(colIdx:(colIdx+14))=d';
            colIdx=colIdx+15;
        end
        data(++lineIdx,:)=line;
    end
end
