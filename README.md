# Finding Movie Couples

This is a FunnyQT implementation of the Henshin transformation discussed by
Christian Krause at
http://www.ckrause.org/2013/12/path-constraints-in-henshin.html.

The goal is to find pairs of Actors/Actresses that act together in at least
three different movies and create a Couple node for each such pair.

The metamodel is depicted here:

http://3.bp.blogspot.com/-hwtr-KzXmak/UrS0ImFPs8I/AAAAAAAAATU/hryneYKhmxU/s1600/movies.png

## Differences and Commonalities with the Henshin Solution

- The Henshin solution uses `*`-actions such that the rule is applied to all
  matches at once.  This corresponds to the FunnyQT `^:forall` metadata
  annotation for rules.

- It seems Henshin defaults to isomorphic matching, i.e., two nodes in the
  pattern cannot be matched to the same node in the model.  With FunnyQT's
  pattern matching DSL, such homomorphic matches are allowed, so the pattern
  contains a constraint that `p1` and `p2` are not equal.

- The Henshin solution uses `<<require*>>` for matching the three movies which
  somehow means that at least the three movies have to exist, but we don't want
  to generate a separate match for every combination of movies.

  FunnyQT's pattern matching DSL has no direct counterpart, but it doesn't
  really need to, anyway.  So the pattern part of the FunnyQT rule simply
  ensures that the two actors/actresses act in at least one common movie, and
  that they in fact act in three common movies is enforced using a predicate
  (`three-common-movies?`).

- The Henshin solution has the slight subtlety.  For every movie couple
  consisting of the persons p1 and p2, actually two Couple nodes are created:
  one for (p1, p2), and one for (p2, p1).  To fix this issue, one could enforce
  an alphabetical order of the persons' names using an attribute condition,
  however in the test models, there are no attributes at all.

  The FunnyQT solution does the right thing, here.  The pattern uses an `:as`
  clause to specify that each match should be reported as a **set** `#{p1 p2}`
  (so that the order is insignificant), and the `:distinct` modifier ensures
  that the pattern reports no match twice.

- The FunnyQT pattern part of the rule declares the node type `Person` only for
  `p1`, but it omits the types for the anonymous node between `p1` and `p2`,
  and for `p2` itself.  However, their types (`Movie` and `Person`) are clear
  the traversed references (`movies` and `persons`) and the metamodel.
  Omitting the types that are clear results in fewer type checks in the code
  that realizes the pattern matching process thus making it faster.  And as
  said, those type checks would be tautologies anyway.

## Performance Comparison

Both the Henshin and the FunnyQT solution perform about equally well (scale
linearly for the provided test models), most probably because their pattern
matching approach is pretty similar.  Although not clearly stated in
Christian's blog post linked above, it sounds to me that Henshin's Giraph code
generator generates code that does a breadth- or depth-first search starting at
some person node.

FunnyQT patterns are also matched by transforming the textual pattern DSL into
a comprehension that effectively does a depth-first search anchored at the node
that occurs first in the pattern, i.e., for every Person `p1` all nodes `p2`
connected via one `movies` reference followed by a `persons` referenced are
searched one after the other.

The following table shows the pure transformation runtimes (excluding model
load time, and after a warmup run) for the provided test models ranging from
the size of 170,000 nodes to 1,700,000 nodes.  All tests were run on my 5 years
old ThinkPad with dual-core 2.1 GHz CPU (but none of the solutions is
multi-threaded anyway), and the JVM process was given 2 GB maximal heap space.

| Model     | Henshin Time | FunnyQT Time |
|-----------+--------------+--------------|
| 170,000   | 3.3 sec      | 2.2 sec      |
| 340,000   | 7.3 sec      | 4.4 sec      |
| 510,000   | 5.7 sec      | 6.9 sec      |
| 680,000   | 7.2 sec      | 10.7 sec     |
| 850,000   | 14.1 sec     | 12.8 sec     |
| 1,020,000 | 17.4 sec     | 14.7 sec     |
| 1,190,000 | 19.4 sec     | 17.4 sec     |
| 1,360,000 | 13.2 sec     | 14.1 sec     |
| 1,530,000 | 15.9 sec     | 17.0 sec     |
| 1,700,000 | 17.8 sec     | 17.6 sec     |

You can run the FunnyQT solution simply by cloning this repository and doing:

```
$ cd funnyqt-movie-couples        # Change into the cloned repo directory
$ tar xvJf example-models.tar.xz  # Unzip the example models
$ lein test                       # Run the benchmark
```

You need the `lein` script from http://leiningen.org.

## License

Copyright © 2013 Tassilo Horn <horn@uni-koblenz.de>

Distributed under the GNU General Public License, version 3 or (at your option)
any later version.
