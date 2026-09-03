/* ==================================================
   Smart NGO Dashboard - Chart.js Dynamic Integration
   ================================================== */

document.addEventListener('DOMContentLoaded', function () {
    initDonationsOverTimeChart();
    initDonationsByCategoryChart();
});

function initDonationsOverTimeChart() {
    const canvas = document.getElementById('donationsOverTimeChart') || document.getElementById('donationsTimeChart');
    if (!canvas) return;

    fetch('/api/dashboard/donations-chart')
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            const ctx = canvas.getContext('2d');
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: data.labels && data.labels.length ? data.labels : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                    datasets: [{
                        label: 'Donations (₹)',
                        data: data.data && data.data.length ? data.data : [5000, 12000, 18000, 25000, 32000, 40000],
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
    const canvas = document.getElementById('donationsByCategoryChart') || document.getElementById('donationsCategoryChart');
    if (!canvas) return;

    fetch('/api/dashboard/categories-chart')
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            const labels = (data && Object.keys(data).length) ? Object.keys(data) : ['EDUCATION', 'HEALTH', 'ENVIRONMENT'];
            const values = (data && Object.values(data).length) ? Object.values(data) : [40000, 30000, 20000];
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
