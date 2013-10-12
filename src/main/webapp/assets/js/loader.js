function getSongs(files) {
	var songlist = new Array();
	var queue = [];
	var mp3 = canPlay('audio/mpeg;'), ogg = canPlay('audio/ogg; codecs="vorbis"');
	for ( var i = 0; i < files.length; i++) {

		var file = files[i];
		var path = file.webkitRelativePath || file.mozFullPath || file.name;

		if (path.indexOf('.AppleDouble') != -1) {
			// Meta-data folder on Apple file systems, skip
			continue;
		}

		// Checks filesize
		var size = file.size || file.fileSize || 4096;
		if (size < 4095) {
			// Most probably not a real MP3
			console.log(path);
			continue;
		}

		// Index of mp3
		if (file.name.indexOf('mp3') != -1) {
			if (mp3) {
				queue.push(file);
			}
		}

		// Index of ogg
		if (file.name.indexOf('ogg') != -1 || file.name.indexOf('oga') != -1) {
			if (ogg) {
				queue.push(file);
			}
		}

		var process = function() {

			if (queue.length) {
				var f = queue.shift();
				parseFile(f, function(tags) {
					
//					var JSONtags = tags;
//					var $tags = "foo";
//					$tags = $(JSONtags); 
//					$title =$(JSONtags.title);
//					var JSONsongs = {
//						"songs" : []
//					};
//					$tags.each(function(index) {
//						var $tags = $(this).text().split('(');
//						var $title = $tags[0];
//						//var $artist = $tags[1].split(')')[0];
//						//var $album = $tags[2].split(')')[1];
//						//var $genre = $tags[3].split(')')[2];
//						JSONsongs.JSONtags.push({
//							"title" : $title,
//							//"artist" : $artist,
//							//"album" : $album,
//							//"genre" : $genre
//						});
//						
//						
//					});
//
//					$.ajax({
//						type : "POST",
//						url : "Loader",
//						data : JSONsongs,
//						success : function() {
//							alert("entered success function");
//						}
//					});

//					document.writeln("Request sent:"
//							+ jsontags.toLocaleUpperCase() + "<br/>");

					process();
				});

				var lq = queue.length;
				setTimeout(function() {
					if (queue.length == lq) {
						process();
					}
				}, 300);

			}
		};
		process();

	}
	console.log(JSON.stringify(songlist));
}// end get songs

/*
 * PARSE FILE FUNCTION
 */
function parseFile(file, callback) {
	if (localStorage[file.name])
		return callback(JSON.parse(localStorage[file.name]));
	ID3v2.parseFile(file, function(tags) {
		// to not overflow localstorage
		localStorage[file.name] = JSON.stringify({
			Title : tags.Title,
			Artist : tags.Artist,
			Album : tags.Album,
			Genre : tags.Genre
		});
		console.log(Title + Artist + Album + Genre);
		callback(tags);
	});
}

/*
 * BROWSER CAPABILITIES FUNCTION
 */
function canPlay(type) {
	var a = document.createElement('audio');
	return !!(a.canPlayType && a.canPlayType(type).replace(/no/, ''));
}

/*
 * doPost
 */
