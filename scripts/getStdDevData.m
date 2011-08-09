function data = getStdDevData(first, last, trialType)

    data=[];
    for i=first:last
        dirname=['randVol_',num2str(i)];
        x=mean(load([dirname,'/fit_',trialType,'_ssd_act.dat']));
        data=[data,x];
    end
end
