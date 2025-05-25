export default class {
    constructor({
                    cardSelector,
                    radioButtonName,
                    leftButtonSelector,
                    rightButtonSelector,
                    activeClass = 'active',
                    hiddenRightClass = 'hidden-right',
                    hiddenLeftClass = 'hidden-left',
                    hiddenMiddleClass = 'hidden-middle',
                    cycleInterval = 3000,
                    debounceInterval = 700
                }) {
        this.currentIndex = 0;
        this.cardSelector = cardSelector;
        this.radioButtonName = radioButtonName;
        this.leftButtonSelector = leftButtonSelector;
        this.rightButtonSelector = rightButtonSelector;
        this.activeClass = activeClass;
        this.hiddenRightClass = hiddenRightClass;
        this.hiddenLeftClass = hiddenLeftClass;
        this.hiddenMiddleClass = hiddenMiddleClass;
        this.cycleInterval = cycleInterval;
        this.debounceInterval = debounceInterval;
    }

    init() {
        this.cards = document.querySelectorAll(this.cardSelector);
        this.totalItems = this.cards.length;
        this.radioButtons = document.querySelectorAll(`input[name="${this.radioButtonName}"]`);
        this.leftButton = document.querySelector(this.leftButtonSelector);
        this.rightButton = document.querySelector(this.rightButtonSelector);
        this.setupEventListeners();
        this.startCycling();
    }

    cycleCards() {
        this.cards.forEach((card, index) => {
            card.classList.remove(this.activeClass, this.hiddenRightClass, this.hiddenLeftClass, this.hiddenMiddleClass);

            if (index === this.currentIndex) {
                card.classList.add(this.activeClass);
            } else if (index === (this.currentIndex + 1) % this.totalItems) {
                card.classList.add(this.hiddenRightClass);
            } else if (index === (this.currentIndex - 1 + this.totalItems) % this.totalItems) {
                card.classList.add(this.hiddenLeftClass);
            } else {
                card.classList.add(this.hiddenMiddleClass);
            }
        });

        if (this.radioButtons.length > 0) {
            this.radioButtons[this.currentIndex].checked = true;
        }
    }

    startCycling() {
        this.intervalId = setInterval(() => {
            this.currentIndex = (this.currentIndex + 1) % this.totalItems;
            this.cycleCards();
        }, this.cycleInterval);
    }

    stopCycling() {
        clearInterval(this.intervalId);
        clearTimeout(this.timeoutId);
    }

    handleRadioButtonChange(event) {
        this.stopCycling();
        this.currentIndex = Array.from(this.radioButtons).indexOf(event.target);
        this.cycleCards();
        this.timeoutId = setTimeout(() => this.startCycling(), this.cycleInterval);
    }

    handleLeftButtonClick() {
        if (this.debounceTimeout) return;
        this.stopCycling();
        this.currentIndex = (this.currentIndex - 1 + this.totalItems) % this.totalItems;
        this.cycleCards();
        this.timeoutId = setTimeout(() => this.startCycling(), this.cycleInterval);
        this.debounceTimeout = setTimeout(() => this.debounceTimeout = null, this.debounceInterval);
    }

    handleRightButtonClick() {
        if (this.debounceTimeout) return;
        this.stopCycling();
        this.currentIndex = (this.currentIndex + 1) % this.totalItems;
        this.cycleCards();
        this.timeoutId = setTimeout(() => this.startCycling(), this.cycleInterval);
        this.debounceTimeout = setTimeout(() => this.debounceTimeout = null, this.debounceInterval);
    }

    setupEventListeners() {
        this.radioButtons.forEach(radio => {
            radio.addEventListener('change', this.handleRadioButtonChange.bind(this));
        });

        if (this.leftButton) {
            this.leftButton.addEventListener('click', this.handleLeftButtonClick.bind(this));
        }

        if (this.rightButton) {
            this.rightButton.addEventListener('click', this.handleRightButtonClick.bind(this));
        }
    }
}
