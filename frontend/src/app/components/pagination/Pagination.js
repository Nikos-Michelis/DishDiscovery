export default class Pagination {
    constructor(metaData, page) {
        this.metaData = metaData;
        this.page = page;
    }
    getHtml() {
        return `
            <section class="pagination-section" id="pagination-section">
                  <div class="pagination-container" id="pagination-container">
                      <button class="previous-btn" id="previous" ${this.page <= 1 ? 'disabled' : ''}><i class="fa-solid fa-arrow-left"></i> Prev</button>
                      <span class="api-paging-text">
                          Page
                          <span class="api-paging-current-pages">${this.page}</span>
                          /
                          <span class="api-paging-total-pages">${this.metaData.totalPages}</span>
                          of
                          <span class="api-paging-total-cards">${this.metaData.totalRows}</span>
                          items
                      </span>
                      <button class="next-btn" id="next" ${this.page >= this.metaData.totalPages ? 'disabled' : ''}>Next <i class="fa-solid fa-arrow-right"></i></button>
                  </div>
            </section>
        `;
    }

    addEventListeners(onPageChange) {
        document.querySelectorAll('.previous-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                console.log(`prev - ${this.page}`);
                if (this.page > 1) {
                    onPageChange(Number(this.page) - 1);
                }
            });

        });

        document.querySelectorAll('.next-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                console.log(`next - ${this.page}`);
                if (this.page < this.metaData.totalPages) {
                    onPageChange(Number(this.page) + 1);
                }
            });

        });
    }

}
