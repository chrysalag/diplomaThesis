### Study and Implementation of Collaborative Filtering Algorithms for the Intelligent Generation of Recommendations under Cold Start Problem

Recommender Systems are software tools and techniques that aim to
help users choose between a wide range of items. This diploma thesis contains 
an overview of the various approaches in the field of recommender
systems. Emphasis was given on the collaborative filtering approach, since
it is considered to be one of the most successful ones, and on the Cold-Start
problem, that is a challenge for the generation of successful recommendations.

Hierarchical Itemspace Rank (HIR) is the algorithm that was chosen to
be implemented. HIR tackles successfully the cold-start problem. It exploits
the innate hierarchical structure of the itemspace to face the sparsity and
cold-start problems and to generate qualitative recommendations.
Except for the algorithm itself, a demo was implemented. This demo
uses HIR to recommend movies to users, using a special for this purpose
dataset. The recommendations are based on the users’ ratings on some
movies.

The algorithm was implemented in Java using LensKit software. LensKit
is a complete system for the implementation, comparison, experimental
evaluation and research on recommender algorithms. It was chosen due to
its features that enable the implementation of algorithms and the handling
of the required data.

This implementation can be part of a new module in LensKit that would
be specialized in facing cold-start problem.

**AREA:** Recommender Systems

**KEYWORDS:** Collaborative Filtering, Cold-Start Problem, HIR, LensKit
