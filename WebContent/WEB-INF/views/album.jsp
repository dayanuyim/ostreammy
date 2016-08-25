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
		<div class=booklet-pos id="booklet-pos-${s.index}"
			ondragenter="dragEnterBooklet(event)" ondragover="dragOverBooklet(event)" ondragleave="dragLeaveBooklet(event)" ondrop="dropBooklet(event)" ></div>

		<div class="booklet-img" id="booklet-img-${s.index}" >
			
			<img src='<app:servPath local="${booklet}" local_base="${localBase}" serv_base="${servBase}" />'
				draggable="true" ondragstart="dragBooklet(event)"
				alt="booklet ${s.count}" >
		</div>
		
		<c:if test="${s.last}">
			<div class=booklet-pos id="booklet-pos-${s.count}"
				ondragenter="dragEnterBooklet(event)" ondragover="dragOverBooklet(event)" ondragleave="dragLeaveBooklet(event)" ondrop="dropBooklet(event)" ></div>
		</c:if>
		
    </c:forEach>
</aside>

<div style="flex:1">
    <div class="album">
		<img src='<app:servPath local="${album.cover}" local_base="${localBase}" serv_base="${servBase}" />'
			alt="Album Cover" id="album-cover">

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
					<audio src='<app:servPath local="${track.location}" local_base="${localBase}" serv_base="${servBase}" />'
            			controls preload="none"></audio>
					-->
					<audio controls preload="none">
						<source type="audio/mpeg" src='<app:servPath local="${track.location}" local_base="${localBase}" serv_base="${servBase}" />' />
					</audio>

					<span class="track-disk-no">
						<i class="fa fa-dot-circle-o"></i>
						<input type="number" name="track_disk_no" min="1" value="${track.diskNo}"/>
					</span>

					<span class="track-no">
						<i class="fa fa-list-ol"></i>
						<input type="number" name="track_no" min="1" value="${track.no}"/>
					</span>

					<span class="track-total-no">
						<i class="fa fa-list-alt"></i>
						<input type="number" name="track_total_no" min="1" value="${track.totalNo}"/>
					</span>
					
					<fieldset>
						<legend>Format Information</legend>

						<div class="track-length">
							<i class="fa fa-clock-o"></i><label>Length</label><fmt:formatDate value="${track.length}" pattern="mm:ss"/>
						</div>

						<div class="track-sha1">
							<i class="fa fa-hashtag"></i><label>SHA1</label><span>${track.sha1}</span>
						</div>

						<div class="track-encoder">
							<i class="fa fa-gavel"></i><label>Encoder</label><span>${track.encoder}</span>
						</div>

						<div class="track-bitrate">
							<i class="fa fa-info"></i><label>Bitrate</label><span>${track.bitrate}</span>
							<span class="track-vbr">${track.vbr? 'V': 'C'}</span>
						</div>

						<div class="track-sample-rate">
							<i class="fa fa-info"></i><label>Sample Rate</label><span>${track.sampleRate}</span>
						</div>

					</fieldset>

					<fieldset>
						<legend>Track Information</legend>

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

						<i class="fa fa-font">
							<input type="text" name="track_tags" placeholder="Tags" value="${track.tags}"/></i>

						<i class="fa fa-font">
							<input type="text" name="track_url" placeholder="url" value="${track.url}"/></i>
					
					</fieldset>

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
//drag-n-drop ==================================
function isAdjacentBookletPos(booklet_id, pos_id)
{
	var booklet = $("#" + booklet_id);
	var pos = $("#" + pos_id);
	return booklet.next().is(pos) || booklet.prev().is(pos);
}

function isBookletTarget(e)
{
    var booklet_id = e.dataTransfer.getData("id");
    var pos_id = e.target.id;
    
    return !isAdjacentBookletPos(booklet_id, pos_id);
}

function dragBooklet(e)
{
    e.dataTransfer.setData("id", e.target.parentElement.id);
}

function dragOverBooklet(e)
{
    if(isBookletTarget(e))
		e.preventDefault();
}

function dragEnterBooklet(e)
{
    if(isBookletTarget(e))
		activateOnly(".booklet-pos", "#" + e.target.id);
}

function dragLeaveBooklet(e)
{
    if(isBookletTarget(e))
		deactivateAll(".booklet-pos");
}

function dropBooklet(e)
{
    if(!isBookletTarget(e))
    	return;

	var img_id = e.dataTransfer.getData("id");
	var pos_id = e.target.id;

	//parse src/dst idx
	var sp = img_id.lastIndexOf('-') + 1;
	var src_idx = parseInt(img_id.substring(sp));

	var sp = pos_id.lastIndexOf('-') + 1;
	var dst_idx = parseInt(pos_id.substring(sp));
	if(src_idx < dst_idx) --dst_idx;

	console.log("drag: " + src_idx + " -> " + dst_idx);
	
	//moving images
    reindexChild('#booklet-img-', src_idx, dst_idx);
	
	//cover
	if(dst_idx == 0 || src_idx == 0)
		$('#album-cover').attr('src', $('#booklet-img-0').find('img').attr('src'))

	//reset ui
	deactivateAll(".booklet-pos");

	e.preventDefault();
}

function rotateBookletRight(start, stop)
{
    rotateChild('#booklet-', start, stop, false);
}

function rotateBookletLeft(start, stop)
{
    rotateChild('#booklet-', start, stop, true);
}

//select tabpages ===============================
function selectDisk(evt, disk_id) {
    selectPage('.disk', evt.currentTarget, disk_id);
}

function selectTrack(evt, track_id) {
    $(".active audio").trigger('pause');
    selectPage('.track', evt.currentTarget, track_id);
}

function selectPage(qualifier, page_elem, content_id)
{
    var page_cls = ".tabpage";
    var content_cls = '.tabcontent';

	activateOnly(qualifier + page_cls, $(page_elem).parent(page_cls)[0]);
	activateOnly(qualifier + content_cls, "#" + content_id);
}

// utils =======================================================================
function activateOnly(sels, active_sel)
{
	$(sels).removeClass("active");
	$(active_sel).addClass("active");
}

function deactivateAll(sels)
{
	$(sels).removeClass("active");
}

function reindexChild(id_pre, src, dst)
{
	if(src < dst)
        rotateChild(id_pre, src, dst, true);  //rotate left
	else if(src > dst)
        rotateChild(id_pre, dst, src, false); //rotate right
}

function rotateChild(id_pre, start, stop, is_left)
{
	if(stop - start <= 0)
		return;

    var step   = is_left? 1: -1;
    var _start = is_left? start: stop;
    var _stop  = is_left? stop: start;
    var _end = _stop + step;

    var start_child = $(id_pre + _start).children().detach();

    for(var i = _start; i != _end; i += step){
        var curr = id_pre + i;
        var next = id_pre + (i + step);
        
        var next_child = (i == _stop)?
        		start_child:
        		$(next).children().detach();
        
        $(curr).append(next_child);
    }
}

</script>
</html>

