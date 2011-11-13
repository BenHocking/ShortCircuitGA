# Launches NeuroJet on cluster in batches
# Hack: strip off the leading /net/uf7/abh2n/fuse
if $# < 2
then
else
script_dir=`dirname ${1:19}/trace_full.nj`
sed -e "s/wxyz/${2}/g"  -e "s|script_dir|${script_dir}|g" ~/GA.jsdl > GA_${2}.jsdl
cp GA_${2}.jsdl ~/fuse/queues/grid-queue/submission-point
rm GA_${2}.jsdl
fi