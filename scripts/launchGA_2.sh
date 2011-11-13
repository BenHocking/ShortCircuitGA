# Launches NeuroJet on cluster in batches
# Hack: strip off the leading /net/uf7/abh2n/fuse
prior_file=~/prior.dat
prior_arg1=
prior_arg2=
if [ -f ${prior_file} ]
then
  while read line
  do
    if (( ${#prior_arg1} > 0 ))
    then
      prior_arg2=${line}
      break
    else
      prior_arg1=${line}
    fi
  done < ${prior_file}
fi
script_dir=`dirname ${1:19}/trace_full.nj`
if (( ${#prior_arg1} > 0 ))
then
  if (( $# > 0 ))
  then
    sed -e "s/wxyz/${2}/g" -e "s|script_dir_1|${prior_arg1}|g" -e "s|script_dir_2|${script_dir}|g" ~/GA_n2.jsdl > GA_n2_${2}.jsdl
    cp GA_n2_${2}.jsdl ~/fuse/queues/grid-queue/submission-point
    rm GA_n2_${2}.jsdl
  else
    # If there are an odd number of jobs run in a generation (not all individuals are evaluated), this code
    #  is executed
    sed -e "s/wxyz/${prior_arg2}/g"  -e "s|script_dir|${prior_arg1}|g" ~/GA.jsdl > GA_${prior_arg2}.jsdl
    cp GA_${prior_arg2}.jsdl ~/fuse/queues/grid-queue/submission-point
    rm GA_${prior_arg2}.jsdl
  fi
  rm ${prior_file}
elif (( $# > 0 ))
then
  echo ${script_dir} > ${prior_file}
  echo $2 >> ${prior_file}
fi
