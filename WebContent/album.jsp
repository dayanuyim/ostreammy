<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="icon" href="/favicon.svg" />
    <link rel="stylesheet" type="text/css" href="/css/album.css" />
    <link rel="stylesheet" type="text/css" href="/css/font-awesome.min.css" />
    <script type="text/javascript" src="/js/jquery.min.js" ></script>

    <title>${album.name}</title>
</head>
<body>

<aside class="booklet">
	<c:forEach var="booklet" items="${album.booklets}" varStatus="diskloop">
		<app:servPath local="${booklet.absolutePath}" local_prefix="${repo.absolutePath}" serv_prefix="${prefixPath}" />
	<%--
		<c:set var="path" value="${fn:substringAfter(booklet.absolutePath, repo.absolutePath)}" />
		<img src="${prefixPath}${fn:replace(path, '\\', '/')}" alt="Album Cover" class="booklet-img">
	--%>
    </c:forEach>
</aside>

<div style="flex:1">
    <div class="album">
		<c:set var="coverPath" value="${fn:substringAfter(album.cover.absolutePath, repo.absolutePath)}" />
		<img src="${prefixPath}${fn:replace(coverPath, '\\', '/')}" alt="Album Cover" class="album-cover">

		<div class="album-info">
			<i class="fa fa-user"></i>
			<input type="text" name="album_artist" placeholder="Artist" value="${album.artist.name}"/>
			<br>

			<i class="fa fa-font"></i>
			<input type="text" name="album_name" placeholder="Name" value="${album.name}"/>
			<br>

			<i class="fa fa-calendar-o"></i>
			<input type="date" name="album_date" placeholder="yyyy-mm-dd" value="${album.publishDate}"/>
			<br>

			<i class="fa fa-building"></i>
			<input type="text" name="album_publisher" placeholder="Publisher" value="${album.publisher}"/>
			<br>

			<i class="fa fa-sticky-note-o"></i>
			<input type="text" name="album_comment" placeholder="Comment" value="${album.comment}"/>
			<br>
        </div>
    </div>

	<!-- disk list -->
    <ul class="disk tab">
		<c:forEach var="disk" items="${album.disks}" varStatus="diskloop">
			<li class="disk tabpage">
				<a href="#" onclick="selectDisk(event, 'disk${diskloop.index}')">
					<i class="fa fa-dot-circle-o"></i>Disk ${disk[0].diskNo}</a>
			</li>
		</c:forEach>
    </ul>

	<!-- disk content -->
	<c:forEach var="disk" items="${album.disks}" varStatus="diskloop">
		<div id="disk${diskloop.index}" class="disk tabcontent">
		
			<!--  track list -->
			<ul class="track tab">
				<c:forEach var="track" items="${disk}" varStatus="trackloop">
					<li class="track tabpage">
						<i class="fa fa-music"></i>
						<a href="#" onclick="selectTrack(event, 'track${diskloop.index}-${trackloop.index}')">${track.title}</a>
					</li>
				</c:forEach>
			</ul>

			<!--  track content -->
			<c:forEach var="track" items="${disk}" varStatus="trackloop">
				<div id="track${diskloop.index}-${trackloop.index}" class="track tabcontent">

					<c:set var="repoPath" value="${fn:substringAfter(track.location.absolutePath, repo.absolutePath)}" />
            		<audio src="${prefixPath}${fn:replace(repoPath, '\\', '/')}" controls loop preload="metadata"></audio>
            		<br>

					<i class="fa fa-font"></i>
					<input type="text" name="track_title" placeholder="Title" value="${track.title}"/>
					<br>

					<c:forEach var="artist" items="${track.artists}">
						<i class="fa fa-user"></i>
						<input type="text" name="track_artist" placeholder="Artists" value="${artist.name}"/>
						<br>
					</c:forEach>

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
    $(".active audio").trigger('pause');
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
        else if (!elem.className.includes(" active"))
	        elem.className += " active";
    }
}

</script>
</html>

