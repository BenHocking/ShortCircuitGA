# Gets run afterwards
script_dir=`dirname $1`
tgz_file=${script_dir}/trace_full.tgz
timer_cnt=0
while [ ${timer_cnt} -le 3000 ]
do
  if [ -f ${tgz_file} ]; then
    break
  fi
  sleep 3
  timer_cnt=$(( $timer_cnt + 3 ))
done
if [ ! -f ${tgz_file} ]; then
  suffix=${1//\//_}
  /home/abh2n/launchGA.sh $script_dir $suffix
  $0 $1
else
  sleep 10 # Give it a chance to finish transferring
  cd ${script_dir}
  tar -xvzf trace_full.tgz
  cd -
fi
