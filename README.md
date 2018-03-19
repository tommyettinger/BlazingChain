BlazingChain
--

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.tommyettinger/blazingchain/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.github.tommyettinger/blazingchain)

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
original Java code. Javadocs are available in the code and on Maven Central, but the method names are
clear and the API surface is small at 8 methods, half for compression and half for decompression. Some
small examples (really, really small) are below.

A preview is [available here on github.io](https://tommyettinger.github.io/BlazingChain/index.html),
which shows the URI-encoding form of compression (mainly because the full range of UTF-16 characters
used by UTF16 mode couldn't be displayed by most web browsers, so the compressed result would be
either unreadable or un-copy-able). The preview runs with GWT, but if you have Strings compressed by
this library using other JVM types (in URI-encoded mode only for now), you can enter the compressed
Strings on the right and click "<- Decompress" to show their contents at left. You can also enter
uncompressed text at left and compress it with "Compress ->", writing to the right pane.

Usage
---

```java
import blazing.chain.LZSEncoding; // or you can import static , this is all static.
...
String longText, compressed, decompressed;
longText = "This is some long, long, long, long, long, repetitive text!";
////These next two lines use the tightest encoding; it can use all of Unicode,
//// but may produce invalid UTF-16 codepoint pairs. It should be noted that
//// invalid pairs can cause a compressed file to be read back incorrectly if
//// it has made a round-trip to the filesystem saved as UTF-16, UTF-8, or
//// possibly any encoding other than binary. If you aren't saving the compressed
//// String as its exact bytes, you should prefer a different pair of methods.
compressed = LZSEncoding.compress(longText);
decompressed = LZSEncoding.decompress(compressed);
////you can try the next line if you want to make sure they really are equal.
//assert(longText.equals(decompressed));

////Other encodings have similar pairings of compress method to decompress method.

////This kind of encoding uses 15 of the 16 bits in a UTF-16 char, but should
//// always produce valid UTF-16. It does not compress quite as well as the first
//// method, but is compatible with various places that primarily use UTF-16.
////This is the recommended way of using the library if files are involved.
////For optimal file size, save files in UTF-16 encoding when compressed this way.
//compressed = LZSEncoding.compressToUTF16(longText);
//decompressed = LZSEncoding.decompressFromUTF16(compressed);

////This kind of encoding uses pure ASCII, specifically the 64 Base64 characters,
//// plus possibly '=' as a suffix.
//compressed = LZSEncoding.compressToBase64(longText);
//decompressed = LZSEncoding.decompressFromBase64(compressed);

////This kind of encoding uses pure ASCII, specifically the 64 characters that
//// are valid in URI component encoding.
//compressed = LZSEncoding.compressToEncodedURIComponent(longText);
//decompressed = LZSEncoding.decompressFromEncodedURIComponent(compressed);
```

Installation
---

You can get this version (which should be compatible with lz-string 1.4.4)
[using this info on Maven Central](http://search.maven.org/#artifactdetails%7Ccom.github.tommyettinger%7Cblazingchain%7C1.4.4.2%7Cjar).
That page provides dependency info for many build tools including Maven, Gradle, Ivy, SBT, and Lein.
There should be a release on GitHub as well. For GWT, you will need this inherits line:

`<inherits name='blazing.chain' />`

Other
---

The name is a play on the LZ in Blazing and LZ-String, and Chain being a String-like object, but is also
a reference to [an obscure, no-longer-canon group](http://starwars.wikia.com/wiki/Blazing_Chain) from
the distant past of a particular far, far away galaxy.

Included for test purposes are a public domain poem in Finnish (Suomi) called "Tuopa tuopi tuiman tunnon",
by August Ahlqvist ([retrieved from Wikisource here](https://fi.wikisource.org/wiki/Tuopa_tuopi_tuiman_tunnon)),
and the third paragraph of the public domain novel "A Princess of Mars" by Edgar Rice Burroughs
([retrieved from Wikisource here](https://en.wikisource.org/wiki/A_Princess_of_Mars/Chapter_I)).
I have no idea what the poem means, but it mixes ASCII and non-ASCII characters so it serves as good test data.
Each has versions in uncompressed form as well as compressed with UTF16, URI Encoding, and Base-64 modes.
The mode corresponding to `compress()` and `uncompress()` is not provided because I don't know how to accurately
write its invalid UTF-16 codepoints to disk.
