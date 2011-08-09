function data = getMeanData(first, last, trialType)

    data=[];
    for i=first:last
        dirname=['randVol_',num2str(i)];
        x=mean(load([dirname,'/fit_',trialType,'_mean_act.dat']));
        data=[data,x];
    end
end
