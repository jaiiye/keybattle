var startTime = null;
var selectedWord = -1;
var totalSymbols = 0;
var totalPrintedSymbols = 0;
var totalMistakes = 0;

function init() {
    document.getElementById('player-input').value = '';
    startTime = null;
    selectedWord = -1;
    totalSymbols = 0;
    totalPrintedSymbols = 0;
    totalMistakes = 0;
    selectNextWord();
}

function submit(event) {
    if (startTime == null) {
        startTime = new Date().getTime();
        document.getElementById('timer').className = '';
        document.getElementById('timer').textContent = '0.00';
        window.setInterval(function () {
            var timePassed = (new Date().getTime() - startTime) / 1000.0;
            document.getElementById('timer').textContent = timePassed.toFixed(2)
        }, 10);
    }
    if (isSpacePressed(event)) {
        var playerWord = document.getElementById('player-input').value;
        if (playerWord.trim().length != 0) {
            var realWord = document.getElementById('player-text-token-' + selectedWord).textContent;

            totalSymbols = totalSymbols + realWord.trim().length + 1;
            totalPrintedSymbols = totalPrintedSymbols + playerWord.trim().length + 1;
            totalMistakes = totalMistakes + Math.abs(levenshtein(playerWord.trim(), realWord.trim()));
            selectNextWord();
        }
        clearPlayerInput();
        event.preventDefault();
    }
}

function isSpacePressed(event) {
    return event.charCode == 32;
}


function selectNextWord() {
    if (selectedWord >= 0) {
        document.getElementById('player-text-token-' + selectedWord).className = '';
    }
    selectedWord = selectedWord + 1;
    var element = document.getElementById('player-text-token-' + selectedWord);
    if (element == null) {
        finished();
    } else {
        document.getElementById('player-text-token-' + selectedWord).className = 'selected-player-word';
        document.getElementById('player-input').placeholder = document.getElementById('player-text-token-' + selectedWord).textContent;
    }
}

function clearPlayerInput() {
    document.getElementById('player-input').value = '';
}

function finished() {
    var endTime = new Date().getTime();
    var totalTime = (endTime - startTime)/1000.0;
    var printingSpeed = (totalPrintedSymbols/totalTime) * 60;
    alert('You have successfully finished the competition!\n' +
        'Total time: ' + totalTime.toFixed(2) + ' sec\n' +
        'Total symbols in text: ' + totalSymbols + '\n' +
        'Total symbols you printed: ' + totalPrintedSymbols + '\n' +
        'Total mistakes you made: ' + totalMistakes + '\n' +
        'Your printing speed: ' + printingSpeed.toFixed(2) + ' sym/min\n' +
        'Congratulations! Try it again!');
    location.reload();
}

function levenshtein(s1, s2, costs) {
    var i, j, l1, l2, flip, ch, chl, ii, ii2, cost, cutHalf;
    l1 = s1.length;
    l2 = s2.length;

    costs = costs || {};
    var cr = costs.replace || 1;
    var cri = costs.replaceCase || costs.replace || 1;
    var ci = costs.insert || 1;
    var cd = costs.remove || 1;

    cutHalf = flip = Math.max(l1, l2);

    var minCost = Math.min(cd, ci, cr);
    var minD = Math.max(minCost, (l1 - l2) * cd);
    var minI = Math.max(minCost, (l2 - l1) * ci);
    var buf = new Array((cutHalf * 2) - 1);

    for (i = 0; i <= l2; ++i) {
        buf[i] = i * minD;
    }

    for (i = 0; i < l1; ++i, flip = cutHalf - flip) {
        ch = s1[i];
        chl = ch.toLowerCase();

        buf[flip] = (i + 1) * minI;

        ii = flip;
        ii2 = cutHalf - flip;

        for (j = 0; j < l2; ++j, ++ii, ++ii2) {
            cost = (ch === s2[j] ? 0 : (chl === s2[j].toLowerCase()) ? cri : cr);
            buf[ii + 1] = Math.min(buf[ii2 + 1] + cd, buf[ii] + ci, buf[ii2] + cost);
        }
    }
    return buf[l2 + cutHalf - flip];
}
