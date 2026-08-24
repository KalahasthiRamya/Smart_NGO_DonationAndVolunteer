/* ==================================================
   Smart NGO Dashboard - Chart.js Dynamic Integration
   ================================================== */

document.addEventListener('DOMContentLoaded', function () {
    initDonationsOverTimeChart();
    initDonationsByCategoryChart();
});

function initDonationsOverTimeChart() {
    const canvas = document.getElementById('donationsOverTimeChart');
    if (!canvas) return;

    fetch('/api/dashboard/donations-chart')
        .then(response => response.json())
        .then(data => {
            const ctx = canvas.getContext('2d');
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: data.labels,
                    datasets: [{
                        label: 'Donations (₹)',
                        data: data.data,
                        borderColor: '#10b981',
                        backgroundColor: 'rgba(16, 185, 129, 0.1)',
                        borderWidth: 3,
                        fill: true,
                        tension: 0.35,
                        pointBackgroundColor: '#10b981',
                        pointRadius: 5
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { display: false }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: { color: '#f1f5f9' },
                            ticks: {
                                callback: function(value) {
                                    return '₹' + value.toLocaleString('en-IN');
                                }
                            }
                        },
                        x: {
                            grid: { display: false }
                        }
                    }
                }
            });
        })
        .catch(err => console.error("Error loading line chart data:", err));
}

function initDonationsByCategoryChart() {
    const canvas = document.getElementById('donationsByCategoryChart');
    if (!canvas) return;

    fetch('/api/dashboard/categories-chart')
        .then(response => response.json())
        .then(data => {
            const labels = Object.keys(data);
            const values = Object.values(data);
            const ctx = canvas.getContext('2d');

            new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: labels,
                    datasets: [{
                        data: values,
                        backgroundColor: ['#3b82f6', '#10b981', '#8b5cf6', '#f59e0b'],
                        borderWidth: 2,
                        borderColor: '#ffffff'
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: { padding: 20, usePointStyle: true }
                        }
                    },
                    cutout: '65%'
                }
            });
        })
        .catch(err => console.error("Error loading doughnut chart data:", err));
}
