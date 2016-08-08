<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="icon" href="/favicon.svg" />
    <link rel="stylesheet" type="text/css", href="/css/album.css" />
    <title>${album.name}</title>
</head>
<body>

<aside class="booklet">
    <div>Cover</div>
    <div>1</div>
    <div>2</div>
</aside>

<div style="flex:1">
    <div class="album">
        <label>Artist</label>
        <input type="text" name="album_artist" placeholder="Artist" value="${album.artist.name}"/>
        <br>

        <label>Album Name</label>
        <input type="text" name="album_name" placeholder="Name" value="${album.name}"/>
        <br>

        <label>Publish Date</label>
        <input type="datetime" name="album_date" placeholder="yyyy-mm-dd" value="${album.publishDate}"/>
        <br>

        <label>Publisher</label>
        <input type="text" name="album_publisher" placeholder="Publisher" value="${album.publisher}"/>
        <br>

        <label>Comment</label>
        <input type="textarea" name="album_comment" placeholder="Comment" value="${album.comment}"/>
        <br>
    </div>

    <ul class="disk tab">
		<c:forEach var="disk" items="${album.disks}">
			<li class="disk tabpage"><a href="#" onclick="selectDisk(event, 'disk${disk[0].diskNo}')">Disk ${disk[0].diskNo}</a></li>
		</c:forEach>
    </ul>

	<c:forEach var="disk" items="${album.disks}">
		<div id="disk${disk[0].diskNo}" class="disk tabcontent">
		
			<!--  track list -->
			<ul class="track tab">
				<c:forEach var="track" items="${disk}">
					<li class="track tabpage"><a href="#"
						onclick="selectTrack(event, 'track${track.no}')">${track.title}</a></li>
				</c:forEach>
			</ul>

			<!--  track content -->
			<c:forEach var="track" items="${disk}">
				<div id="track${track.no}" class="track tabcontent">
					<h3>${track.title}</h3>
					<p>${track.comment}</p>
				</div>
			</c:forEach>

		</div>
	</c:forEach>
	<!--
    <ul class="disk tab">
        <li class="disk tabpage"><a href="#" onclick="selectDisk(event, 'disk1')">Disk I</a></li>
        <li class="disk tabpage"><a href="#" onclick="selectDisk(event, 'disk2')">Disk II</a></li>
    </ul>

    <div id="disk1" class = "disk tabcontent">
        <ul class="track tab">
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track1')">1. 修罗の花</a></li>
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track2')">2. Song2</a></li>
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track3')">3. Song3</a></li>
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track1')">4. Song4</a></li>
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track2')">5. Song5</a></li>
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track3')">6. Song6</a></li>
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track1')">7. Song7</a></li>
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track2')">8. Song8</a></li>
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track3')">9. Song9</a></li>
            <li class="track tabpage"><a href="#" onclick="selectTrack(event, 'track1')">10. Song10</a></li>
        </ul>

        <div id="track1" class="track tabcontent">
            <h3>track1</h3>
            <p>track1 is the capital city of England.</p>
            <audio src="/repo/pop/梶芽衣子/修罗の花.mp3" controls loop preload="metadata"></audio>
        </div>

        <div id="track2" class="track tabcontent">
            <h3>track2</h3>
            <p>track2 is the capital of France.</p>
        </div>

        <div id="track3" class="track tabcontent">
            <h3>track3</h3>
            <p>track3 is the capital of Japan.</p>
        </div>
    </div>

    <div id="disk2" class = "disk tabcontent">
        <ul class="track tab">
            <li class="tabpage"><a href="#" onclick="selectTrack(event, 'track2-1')">1. Song2-1</a></li>
            <li class="tabpage"><a href="#" onclick="selectTrack(event, 'track2-2')">2. Song2-2</a></li>
        </ul>
        <div id="track2-1" class="track tabcontent"> <h3>track2-1</h3> </div>
        <div id="track2-2" class="track tabcontent"> <h3>track2-2</h3> </div>
    </div>
	-->
</div>

</body>
<script>
function selectDisk(evt, disk_id) {
    selectPage(evt, disk_id, 'disk');
}

function selectTrack(evt, track_id) {
    selectPage(evt, track_id, 'track');
}

function selectPage(page_evt, content_id, type) {
    //hightlight TabPage of type
    selectOnly(type + " tabpage", page_evt.currentTarget.parentElement);

    //highlight TabContent of type
    selectOnly(type + " tabcontent", document.getElementById(content_id));
}

function selectOnly(class_name, active_elem)
{
    var elems = document.getElementsByClassName(class_name);
    for (var i = 0; i < elems.length; i++) {
        var elem = elems[i];
        if(elem !== active_elem)
            elem.className = elem.className.replace(" active", "");
        else if(elem.className.indexOf(" active") < 0)
            elem.className += " active";
    }
}

</script>
</html>

