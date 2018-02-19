function show(i, len) {
    for (var w = 1; w <= len; w++) {
        document.getElementById("link"+w).style.display = 'block';
        document.getElementById("input"+w).style.display = 'none';
    }

    document.getElementById("link"+i).style.display = 'none';
    document.getElementById("input"+i).style.display = 'block';
}