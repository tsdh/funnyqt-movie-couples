# Finding Movie Couples

This is a [FunnyQT](http://jgralab.github.io/funnyqt/) implementation of the
Henshin transformation discussed by Christian Krause at
http://www.ckrause.org/2013/12/path-constraints-in-henshin.html.

This FunnyQT solution is discussed in more details (and compared with the
original Henshin solution) in
[this blog posting](http://tsdh.wordpress.com/2014/01/03/finding-movie-couples-henshin-vs-funnyqt/).

You can run the FunnyQT solution simply by cloning this repository and doing:

```
$ cd funnyqt-movie-couples        # Change into the cloned repo directory
$ tar xvJf example-models.tar.xz  # Unzip the example models
$ lein test                       # Run the benchmark
```

You need the `lein` script from http://leiningen.org.

## License

Copyright © 2014 Tassilo Horn <horn@uni-koblenz.de>

Distributed under the GNU General Public License, version 3 or (at your option)
any later version.
