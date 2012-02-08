Overview
========

ShortCircuitGA contains genetic algorithms with a focus on the ability to combine multiple, expensive fitness functions with simpler, quicker fitness functions that can operate as a proxy in cases where fitness is poor.
An analogy can be made to biological evolution where organisms with significant defects do not survive very long. The code was designed with a specific fitness function involving a neural network simulation of the
hippocampus in mind (NeuroJet), but can be applied to any fitness function, even those that are not composite in nature.