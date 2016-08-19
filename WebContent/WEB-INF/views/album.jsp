<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	<c:forEach var="booklet" items="${album.booklets}" varStatus="s">
		<img src='<app:servPath local="${booklet}" local_home="${repo}" serv_prefix="${prefixPath}" />'
			alt="booklet${s.count}" class="booklet-img">
    </c:forEach>
</aside>

<div style="flex:1">
    <div class="album">
		<img src='<app:servPath local="${album.cover}" local_home="${repo}" serv_prefix="${prefixPath}" />'
			alt="Album Cover" class="album-cover">

		<div class="album-info">
			<i class="fa fa-user">
				<input type="text" name="album_artist" placeholder="Artist" value="${album.artist.name}"/></i>

			<i class="fa fa-font">
			<input type="text" name="album_name" placeholder="Name" value="${album.name}"/></i>

			<i class="fa fa-calendar-o">
			<input type="date" name="album_date" placeholder="yyyy-mm-dd" value="${album.publishDate}"/></i>

			<i class="fa fa-building">
			<input type="text" name="album_publisher" placeholder="Publisher" value="${album.publisher}"/></i>

			<i class="fa fa-sticky-note-o">
			<input type="text" name="album_comment" placeholder="Comment" value="${album.comment}"/></i>
        </div>
    </div>

	<!-- disk list -->
    <ul class="disk tab">
		<c:forEach var="disk" items="${album.disks}" varStatus="diskloop">
			<li class="disk tabpage">
				<a href="#" onclick="selectDisk(event, 'disk${diskloop.count}')">
					<i class="fa fa-dot-circle-o">Disk ${disk[0].diskNo}</i></a>
			</li>
		</c:forEach>
    </ul>

	<!-- disk content -->
	<c:forEach var="disk" items="${album.disks}" varStatus="diskloop">
		<div id="disk${diskloop.count}" class="disk tabcontent">
		
			<!--  track list -->
			<ul class="track tab">
				<c:forEach var="track" items="${disk}" varStatus="trackloop">
					<li class="track tabpage">
						<a href="#" onclick="selectTrack(event, 'track${diskloop.count}-${trackloop.count}')">
							<i class="fa fa-music">${track.title}</i></a>
					</li>
				</c:forEach>
			</ul>

			<!--  track content -->
			<c:forEach var="track" items="${disk}" varStatus="trackloop">
				<div id="track${diskloop.count}-${trackloop.count}" class="track tabcontent">

					<!--
					<audio src='<app:servPath local="${track.location}" local_home="${repo}" serv_prefix="${prefixPath}" />'
            			controls preload="none"></audio>
					-->
					<audio controls preload="none">
						<source type="audio/mpeg" src='<app:servPath local="${track.location}" local_home="${repo}" serv_prefix="${prefixPath}" />' >
					</audio>

					<i class="fa fa-font">
						<input type="text" name="track_title" placeholder="Title" value="${track.title}"/></i>
						
					<c:forEach var="artist" items="${track.artists}">
						<i class="fa fa-user">
							<input type="text" name="track_artist" placeholder="Artists" value="${artist.name}"/></i>
					</c:forEach>

					<i class="fa fa-user">
						<input type="text" name="track_original_artist" placeholder="Original Artist" value="${track.originalArtist.name}"/></i>

					<i class="fa fa-user">
						<input type="text" name="track_composer" placeholder="Composer" value="${track.composer.name}"/></i>

					<i class="fa fa-list-alt">
						<input type="number" name="track_disk_no" min="1" value="${track.diskNo}"/></i>

					<i class="fa fa-list-ol">
						<input type="number" name="track_no" min="1" value="${track.no}"/>
						of<input type="number" name="track_total_no" min="1" value="${track.totalNo}"/>
					</i>

					<i class="fa fa-clock-o">
						<fmt:formatDate value="${track.length}" pattern="mm:ss"/>
						<input type="hidden" name="track_length" value="${track.length.time}" /></i>

					<i class="fa fa-font">
						<input type="text" name="track_tags" placeholder="Tags" value="${track.tags}"/></i>

					<i class="fa fa-font">
						<input type="text" name="track_url" placeholder="url" value="${track.url}"/></i>

					<i class="fa fa-font">
						<input type="text" name="track_sha1" placeholder="sha1" value="${track.sha1}" readonly /></i>

					<i class="fa fa-font">
						<input type="text" name="track_encoder" placeholder="encoder" value="${track.encoder}"/></i>

					<i class="fa fa-font">
						<input type="text" name="track_bitrate" placeholder="bitrate" value="${track.bitrate}" readonly /></i>

					<i class="fa fa-font">
						<input type="text" name="track_sample_rate" placeholder="sample rate" value="${track.sampleRate}" readonly /></i>

					<i class="fa fa-font">
						<input type="text" name="track_vbr" placeholder="VBR" value="${track.vbr}" readonly /></i>

					<i class="fa fa-sticky-note-o">
						<input type="text" name="track_comment" placeholder="Comment" value="${track.comment}"/></i>

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

