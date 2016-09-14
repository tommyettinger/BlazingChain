BlazingChain
--

This one-file library can be used to compress Java/JVM Strings with LZ-String encoding, as developed by
[pieroxy](https://github.com/pieroxy/lz-string) for JavaScript and continued by rufushuang in a Java port.
This code is a cleaned-up and optimized copy of rufushuang's
[lz-string4java](https://github.com/rufushuang/lz-string4java), and is MIT-licensed like that project.

LZ-String encoding can offer significant compression on UTF-16 Strings, like those in Java or in a web
browser's (tightly constrained) LocalStorage. A simple example of the world "hello" repeated 15 times,
each word followed by one of 15 different hex digits, goes from 90 UTF-16 chars to 23 UTF-16 chars with
the default LZ-String encoding. There are options in the library for Base64 compression and URI component
compression as well, using only the chars possible in those formats at a substantial loss to compression
if storing as UTF-16 (but a slight gain if you can store the Base64 or URI-encoded chars as 6 bits instead
of 16 bits, or an about even comparison with the default UTF-16 compression scheme if you use 8 bits).
UTF-8 does rather well to begin with at lowering content size for ASCII text, so a reduction to about 2/3
as many bytes should be the most expected if you encode as Base64. Since UTF-8 chars waste about 2 bits
per byte when storing Base64 data, the gain is not due to better usage of the individual bits per char,
but rather thanks to the usage of a modified LZW compression on the text. LZW is a type of compression
that does especially well at compressing repetitive data in the .7z archive format (excepting slow and
heavy-weight arithmetic coding techniques, which may do better on file size, .7z with the LZMA algorithm
was the only format I found that could compress a 13GB folder of immensely-repetitive data down to about
60 MB, though less common and similar formats like .xz and .lz also use the same or similar algorithm).
Any patents on LZW seem to have expired and it is common in various software.

This particular version of LZ-String encoding has been optimized on top of rufushuang's optimizations,
removing all boxing of char primitives, almost all boxing of int primitives (only to allow usage of
a generic HashMap with Integer keys), much unnecessary conversion between primitive types, all anonymous
inner classes, and a few other performance tweaks, like appending to one StringBuilder instead of the
earlier approach of making an ArrayList of boxed Characters, appending to that, and then re-appending
each Character to another StringBuilder. If premature optimization is the root of all evil, I need an
exorcist, but thankfully the code is small enough that not too much extra work was needed in the
original Java code. Documentation should be added at some point, but the method names are straightforward
and the API surface is small at 8 methods, half for compression and half for decompression.

The name is a play on the LZ in Blazing and LZ-String, and Chain being a String-like object, but is also
a reference to [an obscure, no-longer-canon group](http://starwars.wikia.com/wiki/Blazing_Chain) from
the distant past of a particular far, far away galaxy.
